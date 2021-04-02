package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.bl.hotel.SearchSortService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.hotel.HotelMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    private final KeyWordsSearchImpl keyWordsSearch;
    private final LevelSortImpl levelSort;
    private final PriceSortImpl priceSort;
    private final RateSortImpl rateSort;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoomService roomService;

    @Autowired
    public HotelServiceImpl(KeyWordsSearchImpl keyWordsSearch, LevelSortImpl levelSort, PriceSortImpl priceSort, RateSortImpl rateSort, HotelMapper hotelMapper) {
        this.keyWordsSearch = keyWordsSearch;
        this.levelSort = levelSort;
        this.priceSort = priceSort;
        this.rateSort = rateSort;
        this.hotelMapper = hotelMapper;
    }


    @Override
    public void addHotel(HotelVO hotelVO) throws ServiceException {
        User manager = accountService.getUserInfo(hotelVO.getManagerId());
        if(manager == null || !manager.getUserType().equals(UserType.HotelManager)){
            throw new ServiceException("管理员不存在或者无权限！创建酒店失败！");
        }
        Hotel hotel = new Hotel();
        hotel.setDescription(hotelVO.getDescription());
        hotel.setAddress(hotelVO.getAddress());
        hotel.setHotelName(hotelVO.getName());
        hotel.setPhoneNum(hotelVO.getPhoneNum());
        hotel.setManagerId(hotelVO.getManagerId());
        hotel.setRate(hotelVO.getRate());
        hotel.setBizRegion(BizRegion.valueOf(hotelVO.getBizRegion()));
        hotel.setHotelStar(HotelStar.valueOf(hotelVO.getHotelStar()));
        hotelMapper.insertHotel(hotel);
    }

    @Override
    public void updateRoomInfo(Integer hotelId, String roomType, Integer rooms) {
        roomService.updateRoomInfo(hotelId,roomType,rooms);
    }

    @Override
    public int getRoomCurNum(Integer hotelId, String roomType) {
        return roomService.getRoomCurNum(hotelId,roomType);
    }

    @Override
    public List<HotelVO> retrieveHotels() {

        return hotelMapper.selectAllHotel();
    }

    @Override
    public HotelVO retrieveHotelDetails(Integer hotelId) {
        HotelVO hotelVO = hotelMapper.selectById(hotelId);
        List<HotelRoom> rooms = roomService.retrieveHotelRoomInfo(hotelId);
        List<RoomVO> roomVOS = rooms.stream().map(r -> {
            RoomVO roomVO = new RoomVO();
            roomVO.setId(r.getId());
            roomVO.setPrice(r.getPrice());
            roomVO.setRoomType(r.getRoomType().toString());
            roomVO.setCurNum(r.getCurNum());
            roomVO.setTotal(r.getTotal());
            return roomVO;
        }).collect(Collectors.toList());
        hotelVO.setRooms(roomVOS);

        return hotelVO;
    }

    @Override
    public List<HotelVO> searchHotels(Integer userId, String keyWords,String factor) {
        List<HotelVO> Hotels;
        if(keyWords!=null){
            Hotels = keyWordsSearch.SearchOrSort(null, keyWords);
        }else{
            Hotels = hotelMapper.selectAllHotel();
        }
        if(factor!=null){
            switch (factor){
                case "星级":
                    Hotels = levelSort.SearchOrSort(Hotels,userId.toString());
                    break;
                case "评价":
                    Hotels = rateSort.SearchOrSort(Hotels,null);
                    break;
                case "价格升序":
                    Hotels = priceSort.SearchOrSort(Hotels,"1");
                    break;
                case "价格降序":
                    Hotels = priceSort.SearchOrSort(Hotels,"0");
                default:
                    break;
            }
        }
        return Hotels;
    }
}

