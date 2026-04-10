package btl.nongnghiep.device.dto;

import btl.nongnghiep.sensordata.dto.DataWebsocketDto;

import java.util.List;

public class DeviceWebSocketDto {
    private String idDevice;
    private List<DataWebsocketDto> dataWebsocketDtos;

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public List<DataWebsocketDto> getDataWebsocketDtos() {
        return dataWebsocketDtos;
    }

    public void setDataWebsocketDtos(List<DataWebsocketDto> dataWebsocketDtos) {
        this.dataWebsocketDtos = dataWebsocketDtos;
    }
}
