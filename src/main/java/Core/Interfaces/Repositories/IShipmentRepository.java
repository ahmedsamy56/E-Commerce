package Core.Interfaces.Repositories;

import Core.Entities.Shipment;

public interface IShipmentRepository extends IGenericRepository<Shipment> {
    Shipment findByOrderId(int orderId);
}
