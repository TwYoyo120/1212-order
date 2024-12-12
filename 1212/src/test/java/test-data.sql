
create table Orders (
        id bigint identity not null,
        buyerId bigint,
        order_date datetime2(6) not null,
        orderNumber varchar(255),
        sellerId bigint,
        status varchar(255),
        total float(53),
        primary key (id)
    )
    
    
    
    create table ShippingInfo (
        id bigint identity not null,
        address varchar(255),
        order_id bigint not null,
        recipient varchar(255),
        status varchar(255),
        tracking_number varchar(255),
        primary key (id)
    )