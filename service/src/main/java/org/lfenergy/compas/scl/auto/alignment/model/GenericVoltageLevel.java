// SPDX-FileCopyrightText: 2021 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.auto.alignment.model;

import org.apache.commons.lang3.StringUtils;
import org.lfenergy.compas.scl.auto.alignment.exception.SclAutoAlignmentException;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.lfenergy.compas.scl.auto.alignment.exception.SclAutoAlignmentErrorCode.NO_VOLTAGE_FOUND_ERROR_CODE;

public class GenericVoltageLevel extends AbstractCompasNameEntity {
    public GenericVoltageLevel(Element element) {
        super(element);
    }

    public List<GenericBay> getBays() {
        return getElementsStream("Bay")
                .map(GenericBay::new)
                .collect(Collectors.toList());
    }

    public Optional<GenericBay> getBusbar(String name) {
        if (StringUtils.isNotBlank(name)) {
            return getElementsStream("Bay")
                    .map(GenericBay::new)
                    .filter(GenericBay::isBusbar)
                    .filter(busbar -> name.equals(busbar.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }

    public double getVoltage() {
        return getElementsStream("Voltage")
                .findFirst()
                .map(this::convertStringToDouble)
                .orElseThrow(() -> new SclAutoAlignmentException(NO_VOLTAGE_FOUND_ERROR_CODE,
                        "No Voltage found for VoltageLevel '" + getName() + "'."));
    }

    public Optional<GenericConductingEquipment> getConductingEquipment(String ceName) {
        if (StringUtils.isNotBlank(ceName)) {
            return getElementsStream("Bay")
                    .map(GenericBay::new)
                    .map(GenericBay::getConductingEquipments)
                    .flatMap(List::stream)
                    .filter(conductingEquipment -> ceName.equals(conductingEquipment.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }
}
