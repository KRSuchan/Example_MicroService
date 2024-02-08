package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {
    @Autowired
    private LicenseService licenseService;

    @RequestMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId ) {

        License license = licenseService.getLicense(licenseId, organizationId);

        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(LicenseController.class)
                        .getLicense(organizationId, licenseId))
                .withSelfRel();

        Link createLicenseLink = WebMvcLinkBuilder.linkTo(methodOn(LicenseController.class)
                        .createLicense(organizationId, license, null))
                .withRel("createLicense");

        Link updateLicenseLink = WebMvcLinkBuilder.linkTo(methodOn(LicenseController.class)
                        .updateLicense(organizationId, license))
                .withRel("updateLicense");

        Link deleteLicenseLink = WebMvcLinkBuilder.linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId()))
                .withRel("deleteLicense");

        license.add(selfLink, createLicenseLink, updateLicenseLink, deleteLicenseLink);

        return ResponseEntity.ok(license);
    }
    @PutMapping
    public ResponseEntity<String> updateLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestBody License request,
            @RequestHeader(value = "Accept-Language", required=false) Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(request, organizationId, locale));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }
}
