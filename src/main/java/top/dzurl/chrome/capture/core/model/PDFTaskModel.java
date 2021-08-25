package top.dzurl.chrome.capture.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDFTaskModel extends TaskModel {

    //是否完整截图
    private boolean full = true;

}
