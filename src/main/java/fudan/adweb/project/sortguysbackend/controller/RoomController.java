package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.AuthRequest;
import fudan.adweb.project.sortguysbackend.controller.request.RoomRequest;
import fudan.adweb.project.sortguysbackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/room")
    public ResponseEntity<?> room(@RequestBody RoomRequest request) {
        long newRoomId = roomService.getAvailableRoomIdAndIncr();
        String roomId = roomService.createRoom(String.valueOf(newRoomId), request.getUsername());
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Map<String, String> map = new HashMap<>();
        map.put("roomId", roomId);
        return builder.body(map);
    }
}
