package com.xxxx.server.controller;


import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IJoblevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/system/basic/joblevel")
@Api(value = "职称管理")
public class JoblevelController {

    @Autowired
    IJoblevelService joblevelService;

    @GetMapping("/")
    @ApiOperation(value = "获取所有职称")
    public List<Joblevel> selectAllJobLevel() {
        return joblevelService.list();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取职称")
    public Joblevel selectOneJobLevel(@PathVariable Integer id) {
        return joblevelService.getById(id);
    }

    @PutMapping("/")
    @ApiOperation(value = "根据id修改职称")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel) {
        if (joblevelService.updateById(joblevel)) {
            return RespBean.success("修改职称成功！");
        }
        return RespBean.error("修改职称失败！");
    }

    @PostMapping("/")
    @ApiOperation(value = "增加一条职称")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel) {
        if (joblevelService.save(joblevel)) {
            return RespBean.success("增加职称成功！");
        }
        return RespBean.error("增加职称失败！");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除职称")
    public RespBean deleteJobLevel(@PathVariable Integer id) {
        if (joblevelService.removeById(id)) {
            return RespBean.success("删除职称成功！");
        }
        return RespBean.error("删除职称失败");
    }

}
