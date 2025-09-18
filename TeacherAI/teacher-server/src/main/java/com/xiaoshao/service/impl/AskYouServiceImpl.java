package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoshao.entity.StudentAnswer;
import com.xiaoshao.exception.BaseException;
import com.xiaoshao.exception.ModelConnectException;
import com.xiaoshao.modelname.SystemModelName;
import com.xiaoshao.prompt.SystemPrompt;
import com.xiaoshao.repository.ChatHistoryRepository;
import com.xiaoshao.service.*;
import com.xiaoshao.vo.ModelQuestionResponseVO;
import com.xiaoshao.vo.ModelsAnswerResponseVO;
import com.xiaoshao.vo.StudentAnswerResponseVO;
import io.micrometer.core.instrument.binder.BaseUnits;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import com.xiaoshao.factory.ModelFactory;

import java.util.ArrayList;
import java.util.List;

import static com.xiaoshao.constant.SystemModelNameConstant.*;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class AskYouServiceImpl implements AskYouService {

    private final ModelFactory modelFactory;
    private final ChatMemory chatMemory;
    private final ICourseService courseService;
    private final IChapterService chapterService;
    private final IQuestionService questionService;
    private final IStudentChapterWenxueValueService studentChapterWenxueValueService;
    private final IStudentAnswerService  studentAnswerService;
    private final IModelAnswerService modelAnswerService;
    private final IStudentImproveAnswerService studentImproveAnswerService;
    private final ObjectMapper objectMapper;
    private final ChatHistoryRepository chatHistoryRepository;
    private final SystemPrompt systemPromptProperties;
    private final SystemModelName systemModelName;
    public ChatClient getChatClient(String modelName, ChatMemory chatMemory,
                                    String systemPrompt){
//        这里有一个问题并不是所有的模型都需要这个参数。TODO修改基本客户端
        try {
            ChatModel chatModel = modelFactory.getChatModel(modelName);
            ChatClient.Builder builder = ChatClient.builder(chatModel)
                    .defaultSystem(systemPrompt)
                    .defaultAdvisors(new SimpleLoggerAdvisor())
                    .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory));
            return builder.build();
        } catch (Exception e) {
            throw new ModelConnectException("模型连接失败"+e);
        }
    }
    // 单模型接口：同步返回完整响应
    public String chat(String modelName, String prompt,
                       String chatId,String systemPrompt) {
        try {
            ChatClient client = getChatClient(modelName,chatMemory,systemPrompt);

            chatHistoryRepository.save(chatId);

            // 直接同步调用并返回完整响应
//        注意一点就是prompt里面是系统级的提示，而user是用户级的问题
            return client.prompt()
                    .user(prompt)
                    .advisors(a->a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                    .call()
                    .content();
        } catch (Exception e) {
            throw new ModelConnectException("模型连接失败"+ e);
        }
    }
    @Override
    public ModelQuestionResponseVO systemChat(String prompt, Long userId, Long courseId, Long chapterId) {
//        这里注意模型的名字最好用system代替防止和前端选择的模型相冲撞来导致记忆混乱
        String chatId=generateChatId(userId, courseId, chapterId,"systemQuestion");
//        得到课程以及章节名称
        CourseChapterNames courseNameAndChapterName =getNamesObject(courseId, chapterId);
        String courseName = courseNameAndChapterName.courseName();
        String chapterName = courseNameAndChapterName.chapterName();
//      替换提示词中的课程名称和章节名称
        String systemPrompt = systemPromptProperties.getSingleModelSystemPrompt()
                .replace("{{courseName}}", courseName)
                .replace("{{chapterName}}", chapterName);

        String response = chat(systemModelName.getSystemModelName(),prompt,chatId,systemPrompt);
        ModelQuestionResponseVO modelQuestionResponseVO = parseJson(response, ModelQuestionResponseVO.class);
        if(modelQuestionResponseVO.getFlag()){
            Long questionId=questionService.createQuestion(prompt,courseId,
                    chapterId,userId,modelQuestionResponseVO.getCognitiveLevel());
            if(studentChapterWenxueValueService.existsRecord(userId,courseId)){
                studentChapterWenxueValueService.updateWenxueValue(userId,courseId,
                       modelQuestionResponseVO.getWenxueValue());
            }else {
                studentChapterWenxueValueService.createRecord(userId,courseId,
                        modelQuestionResponseVO.getWenxueValue());
            }
            modelQuestionResponseVO.setQuestionId(questionId);
            modelQuestionResponseVO.setQuestion(prompt);

        }


        return modelQuestionResponseVO;
    }
    @Override
    public StudentAnswerResponseVO systemChatAnswer(String prompt, Long userId, Long courseId, Long chapterId, String question, Long questionId) {
//        这里注意模型的名字最好用system代替防止和前端选择的模型相冲撞来导致记忆混乱
        String chatId=generateChatId(userId, courseId, chapterId,"systemAnswer");
        //        得到课程以及章节名称
        CourseChapterNames courseNameAndChapterName =getNamesObject(courseId, chapterId);
        String courseName = courseNameAndChapterName.courseName();
        String chapterName = courseNameAndChapterName.chapterName();
//      替换提示词中的问题
        String systemPrompt = systemPromptProperties.getSingleModelSystemPromptAnswer()
                .replace("{{courseName}}", courseName)
                .replace("{{chapterName}}", chapterName)
                .replace("{{question}}", question);
        String response = chat(systemModelName.getSystemModelName(),prompt,chatId,systemPrompt);
        StudentAnswerResponseVO studentAnswerResponseVO = parseJson(response, StudentAnswerResponseVO.class);
        if(studentAnswerResponseVO.getFlag()){
            studentAnswerService.createStudentAnswer(questionId,userId,prompt,
                    studentAnswerResponseVO.getSystemScore());
            if(studentChapterWenxueValueService.existsRecord(userId,courseId)){
                log.info("更新学生课程问学值{}", studentAnswerResponseVO.getWenxueValue());
                studentChapterWenxueValueService.updateWenxueValue(userId,courseId,
                        studentAnswerResponseVO.getWenxueValue());
            }else {
                studentChapterWenxueValueService.createRecord(userId,courseId,
                        studentAnswerResponseVO.getWenxueValue());
            }
        }
        return studentAnswerResponseVO;
    }
    @Override
    public StudentAnswerResponseVO systemChatImproveAnswer(String prompt, Long userId, Long courseId, Long chapterId, String question, Long questionId) {
//        这里注意模型的名字最好用system代替防止和前端选择的模型相冲撞来导致记忆混乱
        String chatId=generateChatId(userId, courseId, chapterId,"systemImproveAnswer");
        StudentAnswer studentAnswer = studentAnswerService.getStudentAnswerById(questionId);
        //        得到课程以及章节名称
        CourseChapterNames courseNameAndChapterName =getNamesObject(courseId, chapterId);
        String courseName = courseNameAndChapterName.courseName();
        String chapterName = courseNameAndChapterName.chapterName();
//      替换提示词中的问题
        String systemPrompt = systemPromptProperties.getSingleModelImproveAnswer()
                .replace("{{answerContent}}", studentAnswer.getAnswerContent())
                .replace("{{systemScore}}",studentAnswer.getSystemScore().toString())
                .replace("{{question}}", question)
                .replace("{{courseName}}", courseName)
                .replace("{{chapterName}}", chapterName);
        String response = chat(systemModelName.getSystemModelName(),prompt,chatId,systemPrompt);
        StudentAnswerResponseVO studentAnswerResponseVO = parseJson(response, StudentAnswerResponseVO.class);
        if(studentAnswerResponseVO.getFlag()){
            Integer systemScore = studentAnswerResponseVO.getSystemScore();
            Long id=studentImproveAnswerService.createStudentImproveAnswer(questionId,userId,systemScore,
                    prompt);
//            设置所创建的改进答案id，进行后续的理由输入
            studentAnswerResponseVO.setId(id);
            if(studentChapterWenxueValueService.existsRecord(userId,courseId)){
                log.info("更新学生课程问学值");
                studentChapterWenxueValueService.updateWenxueValue(userId,courseId,
                        studentAnswerResponseVO.getWenxueValue());
            }else {
                log.info("创建学生课程问学值");
                studentChapterWenxueValueService.createRecord(userId,courseId,
                        studentAnswerResponseVO.getWenxueValue());

            }
        }

        return studentAnswerResponseVO;
    }


    // 多模型接口：同步返回所有模型的完整响应
    @Override
    public List<ModelsAnswerResponseVO> multiModelChat(List<String> modelNames,
                                                                    String question,
                                                                    Long userId,
                                                                    Long courseId,
                                                                    Long chapterId,
                                                                    Long questionId) {
        List<ModelsAnswerResponseVO> results = new ArrayList<>();
        //        得到课程以及章节名称,要不然模型对于问题的回答会出现偏差
//        比如只有问题树是什么，没有章节以及课程的考量的话，模型就可能认为树是植物
        CourseChapterNames courseNameAndChapterName =getNamesObject(courseId, chapterId);
        String courseName = courseNameAndChapterName.courseName();
        String chapterName = courseNameAndChapterName.chapterName();
        for (String modelName : modelNames) {
            String chatId=generateChatId(userId, courseId, chapterId,modelName);
            String systemPrompt = systemPromptProperties.getMultiModelSystemPrompt()
                    .replace("{{courseName}}", courseName)
                    .replace("{{chapterName}}", chapterName);
            String response = chat(modelName, question,chatId,systemPrompt);
            Long id=modelAnswerService.createModelAnswer(questionId,response,modelName);
            ModelsAnswerResponseVO modelsAnswerResponseVO = new ModelsAnswerResponseVO();
            modelsAnswerResponseVO.setModelName(modelName);
            modelsAnswerResponseVO.setResponse(response);
            modelsAnswerResponseVO.setId(id);
            results.add( modelsAnswerResponseVO);
        }
        return results;
    }

    @Override
    public List<String> getAvailableModels(Long courseId) {
        return modelFactory.getAvailableModels(courseId);
    }
//    使用courseService以及chapterService获取课程名称和章节名称以及生成标识ID
    private String generateChatId(
            Long userId,
            Long courseId,
            Long chapterId,
            String modelName
    ) {
        // 获取课程名称和章节名称
        String courseName = courseService.getCourseDetail(courseId).getCourseName();
        String chapterName = chapterService.getChapterDetail(chapterId).getChapterName();

        return String.join("_", userId.toString(), courseName, chapterName, modelName);
    }
    // 自定义数据载体
    public record CourseChapterNames(String courseName, String chapterName) {}

    /**
     * 根据课程ID和章节ID获取对应名称（对象形式返回）
     * @param courseId  课程ID
     * @param chapterId 章节ID
     * @return 包含课程名和章节名的数据对象
     */
    public CourseChapterNames getNamesObject(Long courseId, Long chapterId) {
        return new CourseChapterNames(
                courseService.getCourseDetail(courseId).getCourseName(),
                chapterService.getChapterDetail(chapterId).getChapterName()
        );
    }
//    json解析
//    TODO 这里需要解决一个问题就是如果模型回答的格式不是json格式，那么就会报错，所以需要解决这个问题，应该如何解决这个问题
    /**
     * 泛型方法：将 JSON 字符串解析为指定类型的对象
     * @param jsonStr JSON 字符串
     * @param targetType 目标类型（例如 ModelQuestionResponseVO.class）
     * @param <T> 泛型类型
     * @return 解析后的对象
     */
    public <T> T parseJson(String jsonStr, Class<T> targetType) {
        try {
            return objectMapper.readValue(jsonStr, targetType);
        } catch (JsonProcessingException e) {
            throw new BaseException("JSON 解析失败: " + jsonStr+",失败原因："+e);
        }
    }
//    TODO关于前端所需要的问题判断的功能也可以采取这种方法就是没有用户的交互，直接在后台进行模型的判断，判断结果通过后端函数得出后，直接扔给前端一个固定的回答


}