package com.zhuhodor.myblog.config;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 图床常量类
 */
public interface GiteeImgBedConstant {
    //https://gitee.com/zhubug/image-bed
    //私人令牌
    String TOKEN = "8f3b87dbddef4a8b5a029f4c9b6647d2";

    String OWNER = "zhubug";

    String REPO_NAME = "image-bed";

    //上传图片的msg
    String CREATE_REPOS_MESSAGE = "add img";

    //文件前缀
    String IMG_FILE_DEST_PATH = "/img/" + DateUtil.format(new Date(),"yyyy_MM_dd") + "/";

    /**
     * 新建文件
     * <p>
     * owner*   仓库所属空间地址(企业、组织或个人的地址path)
     * repo*    仓库路径
     * path*    文件的路径
     * content* 文件内容, 要用 base64 编码
     * message* 提交信息
     * <p>
     * %s =>仓库所属空间地址(企业、组织或个人的地址path)  (owner)
     * %s => 仓库路径(repo)
     * %s => 文件的路径(path)
     */
    String CREATE_REPOS_URL = "https://gitee.com/api/v5/repos/%s/%s/contents/%s";
    /**
     * 请求建立page  如果建立了，可以刷新
     * <p>
     * owner*  仓库所属空间地址(企业、组织或个人的地址path)
     * repo*    仓库
     */
    String BUILD_PAGE_URL = "https://gitee.com/api/v5/repos/%s/%s/pages/builds";
    /**
     * TODO： gitpage请求路径
     * 示例："https://gitee.com/quxuecx/TypechoBlogImg/raw/master/";
     */
    String GITPAGE_REQUEST_URL = "https://gitee.com/"+OWNER+"/"+REPO_NAME+"/raw/master/";
}
