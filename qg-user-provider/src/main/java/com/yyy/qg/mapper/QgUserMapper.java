package com.yyy.qg.mapper;

import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.utils.YYY;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
public interface QgUserMapper {

    QgUser getQgUserById(@Param(value = "id") String id)throws Exception;
    List<QgUser> getQgUserListByMap(Map<String, Object> param)throws
            Exception;
    Integer getQgUserCountByMap(Map<String, Object> param)throws
            Exception;
    Integer insertQgUser(QgUser qgUser)throws Exception;
    Integer updateQgUser(QgUser qgUser)throws Exception;
    Integer deleteQgUserById(@Param(value = "id") String id)throws
            Exception;
    Integer batchDeleteQgUser(Map<String, List<String>> params);
    QgUser queryQgUserByPhoneAndPwd(Map<String, String> params);


}
