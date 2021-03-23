CREATE DATABASE java_mysql;

USE java_mysql;

CREATE TABLE funcionarios (
	id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    cpf VARCHAR(15) NOT NULL,
    idade INT NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    setor VARCHAR(50) NOT NULL,
    projeto_atuante VARCHAR(50) NOT NULL
);

DESC funcionarios;

SELECT * FROM funcionarios;