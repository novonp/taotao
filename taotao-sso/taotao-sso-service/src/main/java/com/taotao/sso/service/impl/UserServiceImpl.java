package com.taotao.sso.service.impl;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.result.JsonUtils;
import com.taotao.result.TaotaoResult;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String param, int type) {
        if (1==type){
            TbUser tbUser = tbUserMapper.checkUserName(param);
            //如果用户不为null说明此账号已被注册。账号不可用
            if (tbUser != null){
                return TaotaoResult.build(400,"用户名已存在，请重新输入",false);
            }
        }else if (2==type){
            TbUser tbUser = tbUserMapper.checkPhoneNum(param);
            //如果用户电话号码不为null说明此账号已被注册。账号不可用
            if (tbUser != null){
                return TaotaoResult.build(400,"电话号码已存在，请重新输入",false);
            }
        }else if (3==type){
            TbUser tbUser = tbUserMapper.checkEmail(param);
            //如果用户邮箱不为null说明此账号已被注册。账号不可用
            if (tbUser != null){
                return TaotaoResult.build(400,"邮箱已存在，请重新输入",false);
            }
        }else {
            //用户账号、电话号码、邮箱可以使用
            return TaotaoResult.build(400,"输入有误，请重新输入",false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult createUser(TbUser tbUser) {
        //校验账号是否非空
        if (StringUtils.isBlank(tbUser.getUserName())){
            return TaotaoResult.build(400,"用户名为空");
        }
        //校验密码是否为空
        if (StringUtils.isBlank(tbUser.getPassWord())){
            return TaotaoResult.build(400,"密码为空");
        }
        //校验电话号码是否为空
        if (StringUtils.isBlank(tbUser.getPhone())){
            return TaotaoResult.build(400,"电话号码为空");
        }
        //校验邮箱是否为空
        if (StringUtils.isBlank(tbUser.getEmail())){
            return TaotaoResult.build(400,"邮箱为空");
        }
        //校验账号是否重复
        TaotaoResult result = checkData(tbUser.getUserName(), 1);

        if (!(boolean)result.getData()){
            return TaotaoResult.build(400,"用户名重复");
        }

        //校验电话是否重复
        if (StringUtils.isNotBlank(tbUser.getPhone())){
            result = checkData(tbUser.getPhone(),2);
            if (!(boolean)result.getData()){
                return TaotaoResult.build(400,"电话号码重复");
            }
        }

        //校验邮箱是否重复
        if (StringUtils.isNotBlank(tbUser.getEmail())){
            result = checkData(tbUser.getEmail(),3);
            if (!(boolean)result.getData()){
                return TaotaoResult.build(400,"邮箱重复");
            }
        }
        //代码走到这里，说明才能存进数据库中

        Date date= new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        //密码需要加密  (把密码加密在变成数组)
        tbUser.setPassWord(DigestUtils.md5DigestAsHex(tbUser.getPassWord().getBytes()));
        tbUserMapper.insert(tbUser);
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult loginUser(String userName, String passWord) {
        //校验账号是否为空
        if (StringUtils.isBlank(userName)){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        //校验密码是否为空
        if (StringUtils.isBlank(passWord)){
            return TaotaoResult.build(400,"密码不能为空");
        }
        //密码需要md5加密验证
        TbUser tbUser = tbUserMapper.getUserByUserAndPass(userName, DigestUtils.md5DigestAsHex(passWord.getBytes()));

        if (tbUser == null){
            return TaotaoResult.build(400,"账号密码有误请重新输入");
        }

        //生成随机不重复的字符串  (uuid生成的随机数中间带有一根-  这里是把 - 替换了)
        String token = UUID.randomUUID().toString().replace("-", "");
        //缓存中的用户信息是不需要密码
        tbUser.setPassWord(null);
        jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(tbUser));
        //缓存过期时间
        jedisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);

        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {

        String json = jedisClient.get(USER_INFO + ":" + token);
        if (StringUtils.isBlank(json)){
            return TaotaoResult.build(400,"用户没有登录",false);
        }

        //延长登录时间
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        //是一个字符串类型的json格式 ，页面不会认为 你虽然长得像json 但是你本身是一个String类型的字符串
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);

        return TaotaoResult.ok(tbUser);
    }

    @Override
    public TaotaoResult loginOutUser(String token) {

        Long del = jedisClient.del(USER_INFO + ":" + token);

        if (del == 0){
            return TaotaoResult.build(400,"没有找到你要删除的账号");
        }
        return TaotaoResult.ok();
    }
}
