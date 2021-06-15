package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.AuthRequest;
import fudan.adweb.project.sortguysbackend.controller.request.RoomRequest;
import fudan.adweb.project.sortguysbackend.entity.GarbageSortResultInfo;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.game.RoomInfo;
import fudan.adweb.project.sortguysbackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/room")
    public ResponseEntity<?> createNewRoom(@RequestBody RoomRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getUsername().equals(request.getUsername())){
            Map<String, String> map = new HashMap<>();
            map.put("message", "不能为其他用户创建房间");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
        if (request.getHintsNum() < 0){
            Map<String, String> map = new HashMap<>();
            map.put("message", "提示次数不能为负");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
        long newRoomId = roomService.getAvailableRoomIdAndIncr();
        String roomId = roomService.createRoom(String.valueOf(newRoomId), request.getUsername(), request.getHintsNum(), request.getSid());
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Map<String, String> map = new HashMap<>();
        map.put("roomId", roomId);
        return builder.body(map);
    }

    @GetMapping("/room")
    public ResponseEntity<?> getAllRooms(){
        List<RoomInfo> roomInfoList = roomService.getAllRoomInfo();
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        return builder.body(roomInfoList);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomInfo(@PathVariable("roomId") Integer roomId){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        RoomInfo roomInfo = roomService.getRoomInfo(String.valueOf(roomId));
        return builder.body(roomInfo);
    }
}
