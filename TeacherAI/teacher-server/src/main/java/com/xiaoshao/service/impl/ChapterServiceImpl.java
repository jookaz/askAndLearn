package com.xiaoshao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoshao.dto.ChapterDTO;
import com.xiaoshao.entity.Chapter;
import com.xiaoshao.entity.Course;
import com.xiaoshao.mapper.ChapterMapper;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.service.IChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoshao.service.ICourseService;
import com.xiaoshao.utils.BeanCopyUtil;
import com.xiaoshao.vo.ChapterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 章节表 服务实现类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-23
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements IChapterService {
    @Autowired
    private ICourseService courseService;
    @Override
    public void createChapter(ChapterDTO chapterDTO) {
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDTO, chapter);
        save(chapter);
    }

    @Override
    public void updateChapter(ChapterDTO chapterDTO) {
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDTO, chapter);
        updateById(chapter);
    }

    @Override
    public void deleteChapter(Long chapterId) {
        removeById(chapterId);
    }

    @Override
    public List<ChapterVO> getChapterList() {
        List<Chapter> list = list();
        return BeanCopyUtil.copyListProperties(list, ChapterVO::new);
    }

    @Override
    public ChapterVO getChapterDetail(Long chapterId) {
        Chapter chapter = getById(chapterId);
        if (chapter == null) {
            return null;
        }
        Course  course   = courseService.getById(chapter.getCourseId());

        ChapterVO chapterVO = new ChapterVO();
        BeanUtils.copyProperties(chapter, chapterVO);
        chapterVO.setCourseName(course.getCourseName());
        return chapterVO;
    }

    @Override
    public List<ChapterVO> getChapterListByCourseId(Long courseId) {
        LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chapter::getCourseId, courseId)
                .orderByAsc(Chapter::getChapterOrder); // 按章节顺序排序
        List<Chapter> chapters = list(wrapper);
        return BeanCopyUtil.copyListProperties(chapters, ChapterVO::new);
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chapter::getCourseId, courseId);
        remove(wrapper); // 删除该课程下的所有章节
    }

    @Override
    public PageResult<ChapterVO> queryChaptersPage(ChapterDTO chapterDTO) {
        Page<Chapter> page = chapterDTO.toMpPageDefaultSortByUpdateTime();
        // 分页查询
        page(page);
//       封装返回
        return PageResult.of(page, ChapterVO::new);
    }
    @Override
    public List<Map<String,  Long>> getChapterNames(Long courseId) {
        List<Chapter> list = list();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                // 只保留courseId匹配的Chapter对象
                .filter(chapter -> chapter.getCourseId().equals(courseId))
                .map(chapter->
                {
                    Map<String, Long> map = new HashMap<>();
                    map.put(chapter.getChapterName(), chapter.getId());
                    return map;
                })
                .toList();
    }

}
