package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.bl.hotel.SearchSortService;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.vo.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriceSortImpl implements SearchSortService {

    @Autowired
    private RoomService roomService;

    @Override
    public List<HotelVO> SearchOrSort(List<HotelVO> Hotels, String key) {
        HashMap<HotelVO,Double> AvgPriceMap = new HashMap<>();
        for(HotelVO hotel:Hotels){
            List<HotelRoom> rooms = roomService.retrieveHotelRoomInfo(hotel.getId());
            double avgprice = 0.0;
            for (HotelRoom room : rooms) {
                avgprice += room.getPrice();
            }
            avgprice = avgprice/rooms.size();
            AvgPriceMap.put(hotel,avgprice);
        }
        List<Map.Entry<HotelVO, Double>> list = new ArrayList<>(AvgPriceMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<HotelVO> temp = null;
        for (Map.Entry<HotelVO, Double> hotelVOIntegerEntry : list) {
            temp.add(hotelVOIntegerEntry.getKey());
        }
        if(key.equals("0")){
            return temp;
        }else{
            List<HotelVO> reversetemp = null;
            for(int i=temp.size()-1;i>=0;i--){
                reversetemp.add(temp.get(i));
            }
            return reversetemp;
        }
    }
}
