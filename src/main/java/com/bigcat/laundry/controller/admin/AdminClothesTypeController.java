package com.bigcat.laundry.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bigcat.laundry.common.result.Result;
import com.bigcat.laundry.entity.ClothesType;
import com.bigcat.laundry.service.ClothesTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理后台衣物类型管理控制器
 */
@Api(tags = "管理后台-衣物类型管理")
@RestController
@RequestMapping("/admin/clothes-type")
@RequiredArgsConstructor
public class AdminClothesTypeController {

    private final ClothesTypeService clothesTypeService;

    /**
     * 分页查询衣物类型列表
     */
    @ApiOperation("分页查询衣物类型列表")
    @PostMapping("/page")
    public Result<Page<ClothesType>> page(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页记录数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("衣物类型名称") @RequestParam(required = false) String name) {
        Page<ClothesType> result = clothesTypeService.page(new Page<>(page, pageSize), name);
        return Result.success(result);
    }

    /**
     * 查询所有启用的衣物类型
     */
    @ApiOperation("查询所有启用的衣物类型")
    @GetMapping("/list")
    public Result<List<ClothesType>> list() {
        List<ClothesType> list = clothesTypeService.listEnabled();
        return Result.success(list);
    }

    /**
     * 根据ID查询衣物类型
     */
    @ApiOperation("根据ID查询衣物类型")
    @GetMapping("/{id}")
    public Result<ClothesType> getById(@PathVariable Long id) {
        ClothesType clothesType = clothesTypeService.getById(id);
        return Result.success(clothesType);
    }

    /**
     * 新增衣物类型
     */
    @ApiOperation("新增衣物类型")
    @PostMapping
    public Result<Boolean> save(@RequestBody @Valid ClothesType clothesType) {
        boolean success = clothesTypeService.save(clothesType);
        return Result.success(success);
    }

    /**
     * 修改衣物类型
     */
    @ApiOperation("修改衣物类型")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid ClothesType clothesType) {
        boolean success = clothesTypeService.updateById(clothesType);
        return Result.success(success);
    }

    /**
     * 删除衣物类型
     */
    @ApiOperation("删除衣物类型")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        ClothesType clothesType = clothesTypeService.getById(id);
        clothesType.setDeleted(1);
        boolean success = clothesTypeService.updateById(clothesType);
        return Result.success(success);
    }

    /**
     * 更新衣物类型状态
     */
    @ApiOperation("更新衣物类型状态")
    @PutMapping("/status")
    public Result<Boolean> updateStatus(
            @ApiParam("衣物类型ID") @RequestParam Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        boolean success = clothesTypeService.updateStatus(id, status);
        return Result.success(success);
    }
} 