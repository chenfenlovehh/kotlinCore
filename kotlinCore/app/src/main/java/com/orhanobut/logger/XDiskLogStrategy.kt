package com.orhanobut.logger

import android.os.Looper

/**
 * @author liuzeren
 * @time 2017/11/3    上午10:16
 * @mail lzr319@163.com
 */
class XDiskLogStrategy : DiskLogStrategy {
    constructor(looper: Looper, folder: String, maxFileSize: Int) :super(DiskLogStrategy.WriteHandler(looper,folder,maxFileSize))
}