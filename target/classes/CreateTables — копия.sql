DROP TABLE IF EXISTS public.students_courses;
DROP TABLE IF EXISTS public.students_groups;
DROP TABLE IF EXISTS public.students;
DROP TABLE IF EXISTS public.courses;
DROP TABLE IF EXISTS public.groups;

CREATE TABLE public.groups
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL UNIQUE CHECK (name != '')
);

CREATE TABLE public.students
(
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL CHECK (name != ''),
  last_name VARCHAR(30) NOT NULL CHECK (name != '')
);

CREATE TABLE public.courses
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL CHECK (name != ''),
  description VARCHAR(200) NOT NULL CHECK (name != '')
);

--CREATE TABLE public.students_courses
(
  students_id ,
  course_id
);

--CREATE TABLE public.students_groups
(
  students_id ,
  group_id
);



SELECT groups.id, groups.name, COUNT(*) FROM groups LEFT JOIN students_groups on students_groups.group_id = groups.id GROUP BY groups.id HAVING COUNT(*) <= 1;