package com.vsdata.melsec.message.e;

import com.vsdata.melsec.message.Principal;

/**
 * @author liumin
 */
public interface FrameECommand extends FrameEMessage {

    /**
     * 获取主体
     *
     * @return 主体
     */
    Principal getPrincipal();
}
