package com.example.hotel.bl.hotel;

import com.example.hotel.vo.HotelVO;

import java.util.List;

public interface SearchSortService {

    List<HotelVO> SearchOrSort(List<HotelVO> Hotels, String key);
}
