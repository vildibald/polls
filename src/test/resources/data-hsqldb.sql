-- SET DATABASE SQL SYNTAX PGS TRUE;


-- INSERT INTO roles(id, name)
-- VALUES (1, 'ROLE_USER'),
--        (2, 'ROLE_ADMIN')
-- ON CONFLICT DO NOTHING;

-- MERGE INTO roles USING(
-- VALUES (1, 'ROLE_USER'),
--        (2, 'ROLE_ADMIN')
-- ON roles.id =
-- ) WHEN NOT MATCHED THEN INSERT VALUES
--   (1, 'ROLE_USER'),
--   (2, 'ROLE_ADMIN');

MERGE INTO roles USING(VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN'))
  AS vals(x, name) ON roles.id = vals.id
  WHEN NOT MATCHED THEN INSERT VALUES vals.id, vals.name
--   WHEN MATCHED THEN UPDATE SET r.name = vals.name

