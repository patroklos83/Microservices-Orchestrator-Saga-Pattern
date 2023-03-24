package com.patroclos.dto;

import java.util.List;
import com.patroclos.entity.OrchestratorProcessStep;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrchestratorProcessDTO {
	private String id;
    private String status;
    private List<OrchestratorProcessStep> orchestratorProcessSteps;
    
}