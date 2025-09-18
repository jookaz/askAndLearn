package com.xiaoshao.controller.teacher;

import com.xiaoshao.constant.MessageConstant;
import com.xiaoshao.result.Result;
import com.xiaoshao.utils.AliOssUtil;
import com.xiaoshao.result.Result;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.xiaoshao.constant.MessageConstant.UPLOAD_FAILED;

@Tag(name = "通用接口")
@RestController
@Slf4j
@RequestMapping("/teacher/common")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @Operation(summary = "文件上传（可指定存储目录）")
    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderName", required = false) String folderName) {  // 新增可选参数

        log.info("文件上传:{}，目标目录:{}", file, folderName);
        try {
            // 1. 处理文件夹名称
            String targetFolder = StringUtils.isNotBlank(folderName) ?
                    folderName.trim() : "teacher-not-name";

            // 2. 生成唯一文件名（UUID + 扩展名）
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            if (originalFilename != null && originalFilename.contains(".")) {
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                fileName += extension; // 拼接扩展名
            }

            // 3. 构建OSS存储路径：目标文件夹/文件名
            String objectName = targetFolder + "/" + fileName;

            // 4. 调用OSS工具类上传
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error(UPLOAD_FAILED + ": {}", e.getMessage(), e);
            return Result.error(UPLOAD_FAILED + ": " + e.getMessage());
        }
    }
}
