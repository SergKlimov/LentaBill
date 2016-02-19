SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists cash_machines;

drop table if exists checks;

drop table if exists check_facts;

drop table if exists date_dimensions;

drop table if exists products;

drop table if exists product_facts;

drop table if exists stores;

drop table if exists supplier_dimensions;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists cash_machines_seq;

drop sequence if exists checks_seq;

drop sequence if exists check_facts_seq;

drop sequence if exists date_dimensions_seq;

drop sequence if exists products_seq;

drop sequence if exists product_facts_seq;

drop sequence if exists stores_seq;

drop sequence if exists supplier_dimensions_seq;

