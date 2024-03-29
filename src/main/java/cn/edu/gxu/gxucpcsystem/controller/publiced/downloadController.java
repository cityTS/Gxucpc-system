package cn.edu.gxu.gxucpcsystem.controller.publiced;

import cn.edu.gxu.gxucpcsystem.service.ContestService;
import cn.edu.gxu.gxucpcsystem.service.MedalService;
import cn.edu.gxu.gxucpcsystem.controller.Code;
import cn.edu.gxu.gxucpcsystem.domain.Medal;
import cn.edu.gxu.gxucpcsystem.utils.Re;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author MaoMao
 * @Description
 * @create 2022-09-06 9:25 AM
 */

@RestController
@RequestMapping("/api/public")
@CrossOrigin
public class downloadController {
    @Autowired
    MedalService medalService;

    @Autowired
    ContestService contestService;

    /**
     * 下载预检
     * @param mp body
     * @return
     */

    @PostMapping("/download/checkStatus")
    public Re download(@RequestBody Map<String, String> mp){
        if(mp.get("name") == null || mp.get("number") == null) {
            return new Re(Code.RESOURCE_DISABLE, null, "请填写完整信息");
        }
        
        String fileName = mp.get("number")+ "-" + mp.get("name") +".pdf";
        if(fileName.length() > 45){
            return new Re(Code.RESOURCE_DISABLE,null,"太奇怪了，你的名字过长，请联系管理员查询");
        }
        List<Medal>lsMedal = medalService.queryFile(fileName,mp.get("itemNumber"));
        if(lsMedal.size() == 0){
            fileName = mp.get("name")+ "-" + mp.get("number") +".pdf";
            lsMedal = medalService.queryFile(fileName,mp.get("itemNumber"));
            if(lsMedal.size() != 0){
                return new Re(Code.STATUS_OK,null,"查询成功");
            }
            return new Re(Code.RESOURCE_DISABLE,null,"查询不到有效获奖名单");
        }
        return new Re(Code.STATUS_OK,null,"查询成功");
    }

    /**
     * 下载文件
     * @param mp body
     * @return
     */
    @PostMapping("/download")
    public byte[] down(@RequestBody Map<String, String> mp){
        String fileName = mp.get("number")+ "-" + mp.get("name") +".pdf";
        if(fileName.length() > 45){
            return null;
        }

        byte[] bytes = medalService.getFile(fileName,mp.get("itemNumber"));
        if (bytes == null){
            fileName = mp.get("name")+ "-" + mp.get("number") +".pdf";
            bytes = medalService.getFile(fileName,mp.get("itemNumber"));
        }
        return bytes;
    }

    /**
     * 关键字查询比赛
     * @param query
     * @return id name
     */
    @GetMapping("/download/contests")
    public Re getContests(String query) {
        return new Re(Code.STATUS_OK, contestService.queryContest(query), "查询成功");
    }
}
