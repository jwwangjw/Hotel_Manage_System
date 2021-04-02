package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public class MultiRoomCouponStrategyImpl implements CouponMatchStrategy {
    /**
     * 判断某个订单是否满足某种多间优惠策略
     *
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        if (coupon.getCouponType() == 2 && orderVO.getPrice() >= coupon.getTargetMoney() && coupon.getStatus() == 1 && orderVO.getRoomNum() >= 3) {
            return true;
        }
        return false;
    }

}
