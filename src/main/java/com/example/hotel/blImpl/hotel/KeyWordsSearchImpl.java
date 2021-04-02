package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.bl.hotel.SearchSortService;
import com.example.hotel.data.hotel.HotelMapper;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.vo.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KeyWordsSearchImpl implements SearchSortService {

    char[] keys;
    HashMap<HotelVO,Integer> mapper = new HashMap<>();

    @Autowired
    RoomService roomService;

    @Autowired
    HotelMapper hotelMapper;

    @Override
    public List<HotelVO> SearchOrSort(List<HotelVO> emptyValue, String key) {
        this.keys = key.toCharArray();
        //keySearch
        List<HotelVO> Hotels = null;
        for(int i=0;i<key.length();i++){
            List<HotelVO> partHotels0 = hotelMapper.selectHotelByKey(key.substring(i,i+1));
            List<HotelRoom> partHotels1 = roomService.selectHotelByroomkey(key.substring(i,i+1));
            for(int j=0;j<partHotels1.size();i++){
                partHotels0.add(hotelMapper.selectById(partHotels1.get(j).getHotelId()));
            }
            for (HotelVO hotelVO : partHotels0) {
                //assert false;
                Hotels.add(hotelVO);
            }
        }
        //removeDuplicate
        //assert Hotels != null;
        HashSet<HotelVO> h = new HashSet<>(Hotels);
        Hotels.clear();
        Hotels.addAll(h);
        //sortbysimilarity
        for (HotelVO hotel : Hotels) {
            isMatch(hotel);
        }
        List<Map.Entry<HotelVO, Integer>> list = new ArrayList<>(mapper.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<HotelVO> temp = null;
        for (Map.Entry<HotelVO, Integer> hotelVOIntegerEntry : list) {
            temp.add(hotelVOIntegerEntry.getKey());
        }
        return temp;
    }

    private void isMatch(HotelVO hotel){
        int mark_Match = 0;
        int temp;
        temp = Compare(hotel.getAddress());
        if(temp>mark_Match){mark_Match = temp;}
        temp = Compare(hotel.getBizRegion());
        if(temp>mark_Match){mark_Match = temp;}
        temp = Compare(hotel.getName());
        if(temp>mark_Match){mark_Match = temp;}
        List<HotelRoom> rooms = roomService.retrieveHotelRoomInfo(hotel.getId());
        for (HotelRoom room : rooms) {
            temp = Compare(room.getRoomType().toString());
            if (temp > mark_Match) {
                mark_Match = temp;
            }
        }
        if(mark_Match!=0){
            mapper.put(hotel,mark_Match);
        }
    }

    private int Compare(String words){
        int mark = 0;
        int temp = -1;
        for(int i=0;i<words.length();i++){
            int k = indexOf(words.charAt(i));
            if(k!=-1){
                if(k>=temp){
                    if(k==temp+1){
                        mark+=2;
                    }else{
                        mark+=1;
                    }
                }
                temp = k;
            }
        }
        return mark;
    }

    private int indexOf(char k){
        for(int i=0;i<keys.length;i++){
            if(k==keys[i]){
                return i;
            }
        }
        return -1;
    }

}
