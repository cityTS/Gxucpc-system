package cn.edu.gxu.gxucpcsystem.controller.admin;

import cn.edu.gxu.gxucpcsystem.service.MedalService;
import cn.edu.gxu.gxucpcsystem.controller.Code;
import cn.edu.gxu.gxucpcsystem.domain.Medal;
import cn.edu.gxu.gxucpcsystem.utils.MD5Utils;
import cn.edu.gxu.gxucpcsystem.utils.Re;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author MaoMao
 * @Description
 * @create 2022-09-06 9:07 AM
 */

@RestController
@RequestMapping("/api/admin/contest/file")
@CrossOrigin
public class UploadMedalController {
    @Autowired
    MedalService medalService;

    @Value("${buffSize}")
    private int buffer;
    @PostMapping("/{contestId}")
    public Re upload(@PathVariable int contestId, MultipartFile file) throws IOException {
        Medal medal = new Medal();
        medal.setFiles(file.getBytes());
        int len = file.getBytes().length;
        if(len > buffer){
            return new Re(Code.RESOURCE_DISABLE,null,"文件过大，请压缩后上传");
        }
        medal.setSize(len);
        medal.setFileName(file.getOriginalFilename());
        medal.setDate(System.currentTimeMillis());
        medal.setMd5(MD5Utils.string2MD5(medal.getFiles()));
        medal.setContestId(contestId);
        try {
            List<Medal> medalList = medalService.queryFile(medal.getFileName(), String.valueOf(contestId));
            if (medalList.size()!= 0) {

                medalService.delMedalById(medalList.get(0).get_id());
            }
            medalService.addMedal(medal);
        } catch (Exception e) {
            return new Re(Code.DATABASE_ERROR,null, e.getMessage());
        }

        return new Re(Code.STATUS_OK,null,"上传成功");
    }

}
