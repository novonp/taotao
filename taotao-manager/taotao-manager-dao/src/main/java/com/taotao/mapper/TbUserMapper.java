package com.taotao.mapper;


import com.taotao.pojo.TbUser;

public interface TbUserMapper {
    /**
     * 根据用户账号查询用户信息
     * @param username 用户账号
     * @return 查询出来的账号  或者为  null
     */
    TbUser checkUserName(String username);

    /**
     * 根据用户电话号码查询用户信息
     * @param phone 用户电话号码
     * @return 查询出来的电话号码  或者为 null
     */
    TbUser checkPhoneNum(String phone);

    /**
     * 根据用户邮箱查询用户信息
     * @param email 用户邮箱
     * @return 查询出来的邮箱  或者为 null
     */
    TbUser checkEmail(String email);

    /**
     * 新增用户
     * @param tbUser 用户信息
     */
    void insert(TbUser tbUser);

    /**
     * 根据用户账号密码查询用户信息
     * @param userName 用户账号
     * @param md5DigestAsHex 用户密码
     * @return 返回指定用户账号与密码的用户对象  或者  null
     */
    TbUser getUserByUserAndPass(String userName, String md5DigestAsHex);
}