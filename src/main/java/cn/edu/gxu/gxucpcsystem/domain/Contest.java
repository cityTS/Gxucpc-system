package cn.edu.gxu.gxucpcsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sct
 * @date 2022/7/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    private Integer id;
    private String name;
    private Long signUpBeginTime;
    private Long signUpEndTime;
    private String email;
    private String smtpPassword;
    private Long contestBeginTime;
    private Long contestEndTime;
    private Boolean isDownload;

    /**
     * 数据库中比赛名称的长度
     */
    private final int nameLength = 100;

    /**
     * 数据库中邮箱密钥的长度
     */
    private final int smtpLength = 30;
    public String checkIntegrityCreate() {
        if(name == null || name.isEmpty() || name.length() > nameLength) {
            return "比赛名称错误";
        }
        if(signUpBeginTime == null || signUpBeginTime < 0) {
            return "报名开始时间错误";
        }
        if(signUpEndTime == null || signUpEndTime < signUpBeginTime) {
            return "报名结束时间错误";
        }
        if(email == null || email.isEmpty() || !(email.contains("@") && email.contains(".")) || email.lastIndexOf("@") < email.lastIndexOf(".")) {
            return "邮箱错误";
        }
        if(smtpPassword == null || smtpPassword.isEmpty() || smtpPassword.length() > smtpLength) {
            return "密钥错误";
        }
        if(contestBeginTime == null || contestBeginTime < 0) {
            return "比赛开始时间错误";
        }
        if(contestEndTime == null || contestEndTime < contestBeginTime) {
            return "比赛结束时间错误";
        }
        return null;
    }

    public String checkIntegrityUpdate() {
        if(id == null || id < 0) {
            return "编号错误";
        }
        return checkIntegrityCreate();
    }

}
