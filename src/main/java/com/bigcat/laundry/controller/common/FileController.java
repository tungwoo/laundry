package com.bigcat.laundry.controller.common;

import com.bigcat.laundry.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Api(tags = "通用-文件上传")
@RestController
@RequestMapping("/common/file")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 上传文件
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<String> upload(
            @ApiParam("文件") @RequestParam("file") MultipartFile file,
            @ApiParam("文件类型") @RequestParam(value = "type", defaultValue = "common") String type) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        try {
            // 生成文件保存路径
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String dir = uploadDir + File.separator + type + File.separator + today;

            // 创建目录
            File uploadPath = new File(dir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // 获取文件后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 生成新的文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            String filePath = dir + File.separator + fileName;

            // 保存文件
            file.transferTo(new File(filePath));

            // 返回访问路径（相对路径）
            String accessPath = "/uploads/" + type + "/" + today + "/" + fileName;
            return Result.success(accessPath);
        } catch (IOException e) {
            return Result.error("文件上传失败");
        }
    }
} 