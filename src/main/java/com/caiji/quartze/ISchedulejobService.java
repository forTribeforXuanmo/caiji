package com.caiji.quartze;

import com.caiji.weibo.entity.Schedulejob;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  job父类，传入的Schedulejob的名字用来获取该子类bean的实例，从而执行execute()方法
 * </p>
 *
 * @author lsz
 * @since 2017-09-21
 */
public interface ISchedulejobService extends IService<Schedulejob> {
    void execute();
}
