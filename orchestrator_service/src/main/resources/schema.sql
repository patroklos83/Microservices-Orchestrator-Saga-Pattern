DROP TABLE IF EXISTS orchestratorProcessStep;
DROP TABLE IF EXISTS orchestratorProcess;

CREATE TABLE orchestratorProcess (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    status VARCHAR(50)
);


CREATE TABLE orchestratorProcessStep (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    orchestratorProcess_id VARCHAR(100),
    stepType VARCHAR(50),
    name VARCHAR(100),
    error VARCHAR(10000),
    statusStep VARCHAR(50),
    FOREIGN KEY (orchestratorProcess_id) REFERENCES orchestratorProcess(id)
);