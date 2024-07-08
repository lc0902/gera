package com.alee.gera.mapper;

import com.alee.gera.entity.Application;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    @Insert("insert into application (application_id, application_user_id, application_type, application_text, application_status) values " +
            "(#{applicationId},#{applicationUserId},#{applicationType},#{applicationText},#{applicationStatus})")
    boolean insertApplication(Application application);

    @Select("select * from application where application_user_id = #{applicationUserId} and application_type = #{applicationType} and application_status = 0")
    List<Application> getNotDealApplicationByUserId(@Param("applicationUserId")Integer applicationUserId,@Param("applicationType")String applicationType);

    @Select("select * from application where application_status = 0")
    List<Application> getNotDealApplication();

    @Select("select * from application where application_id = #{applicationId}")
    Application getApplicationById(@Param("applicationId")Integer applicationId);
    @Update("update application set application_status = 1 where application_id = #{applicationId}")
    boolean dealApplicationStatus(@Param("applicationId")Integer applicationId);
}
