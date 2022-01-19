-- zadanie 5
--drop table if exists project_steps;
--drop table if exists projects;

create table projects (
    id int primary key auto_increment,
    description varchar(100) not null
);

create table project_steps (
    id int primary key auto_increment,
    description varchar(100) not null,
    days_to_deadline integer default 0,
    project_id integer --not null
);

alter table project_steps
        add foreign key (project_id) references projects (id);

alter table task_groups add project_id int null;
alter table task_groups
        add foreign key (project_id) references projects (id);