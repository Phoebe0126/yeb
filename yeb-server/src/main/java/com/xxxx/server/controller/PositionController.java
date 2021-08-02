package com.xxxx.server.controller;


import com.xxxx.server.pojo.Position;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IPositionService;
import com.xxxx.server.service.impl.PositionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
@RestController
@RequestMapping("/system/basic/pos")
@Api(value = "职位管理")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @ApiOperation(value = "添加职位信息")
    @PostMapping("/")
    public RespBean addOnePosition(@RequestBody Position position) {
        if (positionService.save(position)) {
            return RespBean.success("添加职位成功！");
        }
        return RespBean.error("添加职位失败!");
    }

    @ApiOperation(value = "根据id查询职位信息")
    @GetMapping("/{id}")
    public Position selectPositionById(@PathVariable Integer id) {
        Position position = positionService.getById(id);
        return position;
    }

    @ApiOperation(value = "查询全部职位信息")
    @GetMapping("/")
    public List<Position> selectPositionById() {
         return positionService.list();
    }

//    @ApiOperation(value = "根据id批量查询职位信息")
//    @GetMapping("/")
//    public RespBean selectPositionById(@RequestBody Integer[] ids) {
//        List<Position> positions = positionService.listByIds(Arrays.asList(ids));
//        return RespBean.success("查询成功", positions);
//    }

    @ApiOperation(value = "根据id修改职位信息")
    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position) {
        if (positionService.updateById(position)) {
            return RespBean.success("修改职位成功");
        }
        return RespBean.error("修改职位失败");
    }

    @ApiOperation(value = "根据id删除职位信息")
    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id) {
        if (positionService.removeById(id)) {
            return RespBean.success("删除职位成功");
        }
        return RespBean.error("删除职位失败");
    }

}
