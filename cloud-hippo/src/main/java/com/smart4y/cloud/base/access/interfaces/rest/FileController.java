package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.interfaces.dtos.file.CreateFileCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.file.ModifyFileCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.file.RbacFilePageQuery;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 文件"})
@RestController
public class FileController extends BaseAccessController {

    @GetMapping("/files/page")
    @ApiOperation(value = "文件:分页")
    public ResultMessage<Page<RbacFile>> getFilesPage(RbacFilePageQuery query) {
        return ok();
    }

    @PostMapping("/files")
    @ApiOperation(value = "文件:添加")
    public ResultMessage<Void> createFile(@RequestBody CreateFileCommand command) {
        return ok();
    }

    @PutMapping("/files/{fileId}")
    @ApiOperation(value = "文件:修改")
    public ResultMessage<Void> modifyFile(@PathVariable("fileId") Long fileId, @RequestBody ModifyFileCommand command) {
        return ok();
    }

    @DeleteMapping("/files/{fileId}")
    @ApiOperation(value = "文件:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeFile(@PathVariable("fileId") Long fileId) {
        return ok();
    }

    @GetMapping("/files/{fileId}")
    @ApiOperation(value = "文件:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacFile> viewFile(@PathVariable("fileId") Long fileId) {
        return ok();
    }
}