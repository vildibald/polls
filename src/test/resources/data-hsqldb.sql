MERGE INTO roles USING(VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN'))
  AS vals(id, name) ON roles.id = vals.id
  WHEN NOT MATCHED THEN INSERT VALUES vals.id, vals.name;
--   WHEN MATCHED THEN UPDATE SET r.name = vals.name
