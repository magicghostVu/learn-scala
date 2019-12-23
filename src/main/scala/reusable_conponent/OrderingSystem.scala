package reusable_conponent

trait OrderingSystem {

    type O <: Order

    type I <: Inventory

    type S <: Shipping

    trait Order {
        def placeOrder(i: I): Unit
    }

    trait Inventory {
        def itemExists(order: O): Boolean


    }

    trait Shipping {
        def scheduleShipping(order: O): Long
    }


    // sử dụng self-type
    // bất cứ class nào sau này kế thừa Odering cũng phải có mix-in với I và S
    trait Ordering {
        this: I with S =>

        def placeOrder(o: O): Option[Long] = {
            if (itemExists(o)) {
                o.placeOrder(this)
                Some(scheduleShipping(o))
            }
            else None
        }

    }


}

