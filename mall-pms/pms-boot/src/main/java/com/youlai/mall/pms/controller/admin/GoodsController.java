package com.youlai.mall.pms.controller.admin;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.youlai.common.result.Result;
import com.youlai.mall.pms.pojo.dto.admin.GoodsFormDTO;
import com.youlai.mall.pms.pojo.dto.admin.GoodsPageDTO;
import com.youlai.mall.pms.pojo.entity.PmsSpu;
import com.youlai.mall.pms.pojo.vo.admin.GoodsDetailVO;
import com.youlai.mall.pms.service.IPmsSpuService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 系统管理_商品信息
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/1/4
 **/
@Api(tags = "系统管理_商品信息")
@RestController
@RequestMapping("/api/v1/goods")
@AllArgsConstructor
public class GoodsController {

    private IPmsSpuService iPmsSpuService;

    @ApiOperation(value = "商品分页列表")
    @GetMapping("/page")
    public Result list(GoodsPageDTO queryDTO) {
        return iPmsSpuService.listFromElasticsearch(queryDTO);
    }

    @ApiOperation(value = "商品详情")
    @ApiImplicitParam(name = "id", value = "商品id", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(
            @ApiParam("商品ID") @PathVariable Long id
    ) {
        GoodsDetailVO goodsDetail = iPmsSpuService.getGoodsById(id);
        return Result.success(goodsDetail);
    }

    @ApiOperation(value = "新增商品")
    @PostMapping
    public Result addGoods(
            @RequestBody GoodsFormDTO goodsForm
    ) throws IOException {
        boolean result = iPmsSpuService.addGoods(goodsForm);
        return Result.judge(result);
    }

    @ApiOperation(value = "修改商品")
    @PutMapping(value = "/{id}")
    public Result update(
            @ApiParam("商品ID") @PathVariable Long id,
            @RequestBody GoodsFormDTO goods
    ) {
        boolean result = iPmsSpuService.updateGoods(goods);
        return Result.judge(result);
    }

    @ApiOperation(value = "删除商品")
    @DeleteMapping("/{ids}")
    public Result delete(
            @ApiParam("id集合,以英文逗号(,)分隔") @PathVariable String ids
    ) {
        boolean result = iPmsSpuService.removeByGoodsIds(Arrays.asList(ids.split(",")).stream().map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        return Result.judge(result);
    }

    @ApiOperation(value = "选择性修改商品")
    @PatchMapping(value = "/{id}")
    public Result patch(
            @ApiParam("商品ID") @PathVariable Long id,
            @RequestBody PmsSpu spu
    ) {
        LambdaUpdateWrapper<PmsSpu> updateWrapper = new LambdaUpdateWrapper<PmsSpu>().eq(PmsSpu::getId, id);
        updateWrapper.set(spu.getStatus() != null, PmsSpu::getStatus, spu.getStatus());
        boolean update = iPmsSpuService.update(updateWrapper);
        return Result.success(update);
    }
}
