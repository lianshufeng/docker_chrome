package top.dzurl.chrome.capture.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public abstract class TaskModel {


    //写出的临时文件
    private String output;


}
