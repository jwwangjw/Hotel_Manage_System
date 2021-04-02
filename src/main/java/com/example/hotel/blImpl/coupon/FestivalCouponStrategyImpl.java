package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
public class FestivalCouponStrategyImpl implements CouponMatchStrategy {
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        if (coupon.getCouponType() == 1 && orderVO.getPrice() >= coupon.getTargetMoney() && coupon.getStatus() == 1) {
            return true;
        }
        return false;
    }
}
