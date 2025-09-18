package com.xiaoshao.service;

import com.xiaoshao.dto.ChapterDTO;
import com.xiaoshao.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoshao.result.PageResult;
import com.xiaoshao.vo.ChapterVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 章节表 服务类
 * </p>
 *
 * @author xiaoshao
 * @since 2025-04-23
 */
public interface IChapterService extends IService<Chapter> {
    void createChapter(ChapterDTO chapterDTO);
    void updateChapter(ChapterDTO chapterDTO);
    void deleteChapter(Long chapterId);
    List<ChapterVO> getChapterList();
    ChapterVO getChapterDetail(Long chapterId);
    List<ChapterVO> getChapterListByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
    PageResult<ChapterVO> queryChaptersPage(ChapterDTO chapterDTO);
    List<Map<String,Long>> getChapterNames(Long courseId);

}
