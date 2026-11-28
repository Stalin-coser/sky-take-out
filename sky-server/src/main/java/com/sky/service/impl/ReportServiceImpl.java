package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        //当前集合用于存放从begin 到 end 范围内的每天日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天的营业额
        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dateList) {

            LocalDateTime beginTime = LocalDateTime.of(date , LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date , LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin" , beginTime);
            map.put("end" , endTime);
            map.put("status" , Orders.COMPLETED);

            Double turnover = orderMapper.sumByMapper(map);
            if(turnover == null) turnover = 0.0;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList , ","))
                .turnoverList(StringUtils.join(turnoverList , ","))
                .build() ;
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //存放在begin和end间的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天的新增用户数量
        List<Integer> newUserList = new ArrayList<>();
        //存放每天的总用户数量
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date , LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date , LocalTime.MAX);

            Map map = new HashMap();
            map.put("end" , endTime);

            //总用户数量
            Integer totalUser = userMapper.countByMap(map);
            map.put("begin" , beginTime);

            //当天新增用户数量
            Integer newUser = userMapper.countByMap(map);

            if(totalUser == null) totalUser = 0;
            if(newUser == null) newUser = 0;

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        return UserReportVO
                .builder()
                .newUserList(StringUtils.join(newUserList , ","))
                .totalUserList(StringUtils.join(totalUserList , ","))
                .dateList(StringUtils.join(dateList , ","))
                .build();
    }
}
