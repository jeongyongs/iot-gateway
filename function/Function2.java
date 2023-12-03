package function;
import java.util.List;
import org.json.JSONObject;
import java.util.function.Function;
import com.nhnacademy.aiot.port.Packet;

public class Function2 implements Function<Packet, List<Packet>> {
    @Override
    public List<Packet> apply(Packet packet) {
        System.out.println("패킷을 전송했습니다.");
        return List.of(packet);
    }
}
