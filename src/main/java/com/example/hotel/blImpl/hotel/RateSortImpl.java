package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.SearchSortService;
import com.example.hotel.vo.HotelVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RateSortImpl implements SearchSortService {

    /**
     *
     * @param Hotels
     * @param key null
     * @return
     */
    @Override
    public List<HotelVO> SearchOrSort(List<HotelVO> Hotels, String key) {
        HashMap<HotelVO, Double> HotelRateMap = new HashMap<>();
        for(HotelVO hotel:Hotels){
            HotelRateMap.put(hotel,hotel.getRate());
        }
        List<Map.Entry<HotelVO, Double>> list = new ArrayList<>(HotelRateMap.entrySet());

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<HotelVO> temp = null;
        for (Map.Entry<HotelVO, Double> hotelVOIntegerEntry : list) {
            temp.add(hotelVOIntegerEntry.getKey());
        }
        return temp;
    }

}
