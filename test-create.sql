create table cash_machines (
  id                        integer not null,
  code                      varchar(255) not null,
  store_id                  integer,
  constraint pk_cash_machines primary key (id))
;

create table checks (
  id                        integer not null,
  origin                    bigint not null,
  cash_machine_id           integer,
  value                     double not null,
  constraint pk_checks primary key (id))
;

create table check_facts (
  id                        integer not null,
  date_id                   integer,
  supplier_id               integer,
  value                     double not null,
  constraint pk_check_facts primary key (id))
;

create table date_dimensions (
  id                        integer not null,
  year                      integer(4) not null,
  month                     integer(2) not null,
  day                       integer(2) not null,
  hour                      integer(2) not null,
  minute                    integer(2) not null,
  second                    integer(2) not null,
  as_millisecond            bigint not null,
  constraint pk_date_dimensions primary key (id))
;

create table products (
  id                        integer not null,
  name                      varchar(255) not null,
  value                     double not null,
  constraint pk_products primary key (id))
;

create table product_facts (
  id                        integer not null,
  date_id                   integer,
  supplier_id               integer,
  product_id                integer,
  value                     double not null,
  quantity                  double not null,
  constraint pk_product_facts primary key (id))
;

create table stores (
  id                        integer not null,
  alias                     varchar(255) not null,
  constraint pk_stores primary key (id))
;

create table supplier_dimensions (
  id                        integer not null,
  store_id                  integer,
  cash_machine_id           integer,
  constraint uq_supplier_dimensions_cash_mach unique (cash_machine_id),
  constraint pk_supplier_dimensions primary key (id))
;

create sequence cash_machines_seq;

create sequence checks_seq;

create sequence check_facts_seq;

create sequence date_dimensions_seq;

create sequence products_seq;

create sequence product_facts_seq;

create sequence stores_seq;

create sequence supplier_dimensions_seq;

alter table cash_machines add constraint fk_cash_machines_store_1 foreign key (store_id) references stores (id) on delete restrict on update restrict;
create index ix_cash_machines_store_1 on cash_machines (store_id);
alter table checks add constraint fk_checks_cashMachine_2 foreign key (cash_machine_id) references cash_machines (id) on delete restrict on update restrict;
create index ix_checks_cashMachine_2 on checks (cash_machine_id);
alter table check_facts add constraint fk_check_facts_date_3 foreign key (date_id) references date_dimensions (id) on delete restrict on update restrict;
create index ix_check_facts_date_3 on check_facts (date_id);
alter table check_facts add constraint fk_check_facts_supplier_4 foreign key (supplier_id) references supplier_dimensions (id) on delete restrict on update restrict;
create index ix_check_facts_supplier_4 on check_facts (supplier_id);
alter table product_facts add constraint fk_product_facts_date_5 foreign key (date_id) references date_dimensions (id) on delete restrict on update restrict;
create index ix_product_facts_date_5 on product_facts (date_id);
alter table product_facts add constraint fk_product_facts_supplier_6 foreign key (supplier_id) references supplier_dimensions (id) on delete restrict on update restrict;
create index ix_product_facts_supplier_6 on product_facts (supplier_id);
alter table product_facts add constraint fk_product_facts_product_7 foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_product_facts_product_7 on product_facts (product_id);
alter table supplier_dimensions add constraint fk_supplier_dimensions_store_8 foreign key (store_id) references stores (id) on delete restrict on update restrict;
create index ix_supplier_dimensions_store_8 on supplier_dimensions (store_id);
alter table supplier_dimensions add constraint fk_supplier_dimensions_cashMac_9 foreign key (cash_machine_id) references cash_machines (id) on delete restrict on update restrict;
create index ix_supplier_dimensions_cashMac_9 on supplier_dimensions (cash_machine_id);


