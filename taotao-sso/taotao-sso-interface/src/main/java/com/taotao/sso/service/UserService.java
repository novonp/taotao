package com.taotao.sso.service;

import com.taotao.pojo.TbUser;
import com.taotao.result.TaotaoResult;

public interface UserService {
    /**
     * 校验用户的账号、手机、邮箱
     * @param param 校验的数据
     * @param type 校验的类型(1.用户名 2.手机号码 3.邮箱)
     * @return TaotaoResult对象  (200表示成功 msg表示返回信息 data表示数据是否可用)
     */
    TaotaoResult checkData(String param,int type);

    /**
     * 添加用户信息
     * @param tbUser
     * @return
     */
    TaotaoResult createUser(TbUser tbUser);

    /**
     * 登录
     * @param userName 账号
     * @param passWord 密码
     * @return TaotaoResult对象  (200表示成功 msg表示返回信息 data表示token)
     */
    TaotaoResult loginUser(String userName,String passWord);

    /**
     *根据token从redis查询用户信息
     * @param token 用户token
     * @return 指定用户信息  或者  null
     */
    TaotaoResult getUserByToken(String token);

    /**
     * 退出
     * @param token 用户token
     * @return
     */
    TaotaoResult loginOutUser(String token);
}
