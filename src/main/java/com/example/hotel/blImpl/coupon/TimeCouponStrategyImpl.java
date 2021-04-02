package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种限时优惠策略
     * @param orderVO
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        if(coupon.getCouponType()==4&&orderVO.getPrice()>=coupon.getTargetMoney()&& LocalDateTime.parse(orderVO.getCreateDate()).isBefore(coupon.getStartTime())&&LocalDateTime.parse(orderVO.getCreateDate()).isBefore(coupon.getEndTime())&&coupon.getStatus()==1) {
            return true;
        }
        return false;
    }
}
