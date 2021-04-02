package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.SearchSortService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.po.Order;
import com.example.hotel.vo.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.hotel.enums.HotelStar.*;

@Service
public class LevelSortImpl implements SearchSortService {

    @Autowired
    OrderMapper orderMapper;
    /**
     * 星级排序
     * @param Hotels
     * @param key 这里是userid转化成string类型
     * @return
     */
    @Override
    public List<HotelVO> SearchOrSort(List<HotelVO> Hotels, String key) {
        List<Order> orders = orderMapper.getUserOrders(Integer.parseInt(key));
        List<Integer> hotelorderedID = null;
        for (Order order : orders) {
            hotelorderedID.add(order.getHotelId());
        }
        //筛选星级
        List<HotelVO> FiveStar = null;
        List<HotelVO> FourStar = null;
        List<HotelVO> ThreeStar = null;
        for (HotelVO hotel : Hotels) {
            if (hotel.getHotelStar().equals(Five.toString())) {
                FiveStar.add(hotel);
            }
            if (hotel.getHotelStar().equals(Four.toString())) {
                FourStar.add(hotel);
            }
            if (hotel.getHotelStar().equals(Three.toString())) {
                ThreeStar.add(hotel);
            }
        }
        //预定过优先策略
        FiveStar = PreOrderSelect(FiveStar,hotelorderedID);
        FourStar = PreOrderSelect(FourStar,hotelorderedID);
        ThreeStar = PreOrderSelect(ThreeStar,hotelorderedID);
        //合并
        List<HotelVO> ans = null;
        for(HotelVO hotel:FiveStar){
            ans.add(hotel);
        }
        for(HotelVO hotel:FourStar){
            ans.add(hotel);
        }
        for(HotelVO hotel:ThreeStar){
            ans.add(hotel);
        }
        return ans;
    }

    private List<HotelVO> PreOrderSelect(List<HotelVO> Hotels,List<Integer> Ids){
        List<HotelVO> temp = null;
        for (HotelVO hotel : Hotels) {
            if (Ids.contains(hotel.getId())) {
                //assert temp != null;
                temp.add(0, hotel);
            } else {
                //assert temp != null;
                temp.add(hotel);
            }
        }
        return temp;
    }
}
