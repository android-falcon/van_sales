package com.dr7.salesmanmanager.Modles;

public class Settings {

    private String IpAddress;
    private int transactionType;
    private  int serial;
    private int taxClarcKind;
    private int priceByCust;
    private int useWeightCase;
    private int allowMinus;
    private int numOfCopy;
    private int salesManCustomers;
    private int minSalePric;
    private int printMethod;
    private int allowOutOfRange;
    private int canChangePrice;
    private int readDiscountFromOffers;
    private int workOnline;
    private int paymethodCheck;
    private int bonusNotAlowed;
    private int noOffer_for_credit;
    private  int amountOfMaxDiscount;
    private  int customer_authorized;
    private  int passowrd_data;
    private int arabic_language;
    private int  hide_qty;
    private int lock_cashreport;
    private String salesMan_name;
    private int priventOrder;
    private int requiNote;
    private int preventTotalDisc;

    public int getPreventTotalDisc() {
        return preventTotalDisc;
    }

    public void setPreventTotalDisc(int preventTotalDisc) {
        this.preventTotalDisc = preventTotalDisc;
    }

    public int getPriventOrder() {
        return priventOrder;
    }

    public void setPriventOrder(int priventOrder) {
        this.priventOrder = priventOrder;
    }

    public int getRequiNote() {
        return requiNote;
    }

    public void setRequiNote(int requiNote) {
        this.requiNote = requiNote;
    }

    public String getSalesMan_name() {
        return salesMan_name;
    }

    public void setSalesMan_name(String salesMan_name) {
        this.salesMan_name = salesMan_name;
    }

    public Settings(String ipAddress, int transactionType, int serial, int taxClarcKind, int priceByCust, int useWeightCase,
                    int allowMinus, int numOfCopy, int salesManCustomers, int minSalePric, int printMethod, int allowOutOfRange,
                    int canChangePrice, int readDiscountFromOffers, int workOnline, int paymethodCheck, int bonusNotAlowed, int noOffer_for_credit,
                    int amountOfMaxDiscount, int customer_authorized, int passowrd_data, int arabic_language, int hide_qty, int lock_cashreport, String salesMan_name) {
        IpAddress = ipAddress;
        this.transactionType = transactionType;
        this.serial = serial;
        this.taxClarcKind = taxClarcKind;
        this.priceByCust = priceByCust;
        this.useWeightCase = useWeightCase;
        this.allowMinus = allowMinus;
        this.numOfCopy = numOfCopy;
        this.salesManCustomers = salesManCustomers;
        this.minSalePric = minSalePric;
        this.printMethod = printMethod;
        this.allowOutOfRange = allowOutOfRange;
        this.canChangePrice = canChangePrice;
        this.readDiscountFromOffers = readDiscountFromOffers;
        this.workOnline = workOnline;
        this.paymethodCheck = paymethodCheck;
        this.bonusNotAlowed = bonusNotAlowed;
        this.noOffer_for_credit = noOffer_for_credit;
        this.amountOfMaxDiscount = amountOfMaxDiscount;
        this.customer_authorized = customer_authorized;
        this.passowrd_data = passowrd_data;
        this.arabic_language = arabic_language;
        this.hide_qty = hide_qty;
        this.lock_cashreport = lock_cashreport;
        this.salesMan_name= salesMan_name;
    }

    public int getHide_qty() {
        return hide_qty;
    }

    public void setHide_qty(int hide_qty) {
        this.hide_qty = hide_qty;
    }

    public int getLock_cashreport() {
        return lock_cashreport;
    }

    public void setLock_cashreport(int lock_cashreport) {
        this.lock_cashreport = lock_cashreport;
    }

    public Settings(){

    }

    public Settings(String ipAddress, int transactionType, int serial, int taxClarcKind, int priceByCust, int useWeightCase, int allowMinus, int numOfCopy,
                    int salesManCustomers, int minSalePric, int printMethod, int allowOutOfRange, int canChangePrice, int readDiscountFromOffers, int workOnline,
                    int paymethodCheck,int bonusNotAlowed, int noOffer_for_credit, int amountOfMaxDiscount, int customer_authorized, int passowrd_data,
                    int arabic_language) {
        IpAddress = ipAddress;
        this.transactionType = transactionType;
        this.serial = serial;
        this.taxClarcKind = taxClarcKind;
        this.priceByCust = priceByCust;
        this.useWeightCase = useWeightCase;
        this.allowMinus = allowMinus;
        this.numOfCopy = numOfCopy;
        this.salesManCustomers = salesManCustomers;
        this.minSalePric = minSalePric;
        this.printMethod = printMethod;
        this.allowOutOfRange = allowOutOfRange;
        this.canChangePrice = canChangePrice;
        this.readDiscountFromOffers = readDiscountFromOffers;
        this.workOnline = workOnline;
        this.paymethodCheck = paymethodCheck;
        this.bonusNotAlowed = bonusNotAlowed;
        this.noOffer_for_credit = noOffer_for_credit;
        this.amountOfMaxDiscount = amountOfMaxDiscount;
        this.customer_authorized = customer_authorized;
        this.passowrd_data = passowrd_data;
        this.arabic_language = arabic_language;
    }

    public int getPassowrd_data() {
        return passowrd_data;
    }

    public void setPassowrd_data(int passowrd_data) {
        this.passowrd_data = passowrd_data;
    }

    public int getArabic_language() {
        return arabic_language;
    }

    public void setArabic_language(int arabic_language) {
        this.arabic_language = arabic_language;
    }

    public int getCustomer_authorized() {
        return customer_authorized;
    }

    public void setCustomer_authorized(int customer_authorized) {
        this.customer_authorized = customer_authorized;
    }

    public int getAmountOfMaxDiscount() {
        return amountOfMaxDiscount;
    }

    public void setAmountOfMaxDiscount(int amountOfMaxDiscount) {
        this.amountOfMaxDiscount = amountOfMaxDiscount;
    }

    public int getBonusNotAlowed() {
        return bonusNotAlowed;
    }

    public int getNoOffer_for_credit() {
        return noOffer_for_credit;
    }

    public void setNoOffer_for_credit(int noOffer_for_credit) {
        this.noOffer_for_credit = noOffer_for_credit;
    }

    public void setBonusNotAlowed(int bonusNotAlowed) {
        this.bonusNotAlowed = bonusNotAlowed;
    }

    public int getPaymethodCheck() {
        return paymethodCheck;
    }

    public void setPaymethodCheck(int paymethodCheck) {
        this.paymethodCheck = paymethodCheck;
    }

    public int getCanChangePrice() {
        return canChangePrice;
    }

    public void setCanChangePrice(int canChangePrice) {
        this.canChangePrice = canChangePrice;
    }

    public int getReadDiscountFromOffers() {
        return readDiscountFromOffers;
    }

    public void setReadDiscountFromOffers(int readDiscountFromOffers) {
        this.readDiscountFromOffers = readDiscountFromOffers;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getTaxClarcKind() {
        return taxClarcKind;
    }

    public void setTaxClarcKind(int taxClarcKind) {
        this.taxClarcKind = taxClarcKind;
    }

    public int getPriceByCust() {
        return priceByCust;
    }

    public void setPriceByCust(int priceByCust) {
        this.priceByCust = priceByCust;
    }

    public int getUseWeightCase() {
        return useWeightCase;
    }

    public void setUseWeightCase(int useWeightCase) {
        this.useWeightCase = useWeightCase;
    }

    public int getAllowMinus() {
        return allowMinus;
    }

    public void setAllowMinus(int allowMinus) {
        this.allowMinus = allowMinus;
    }

    public int getNumOfCopy() {
        return numOfCopy;
    }

    public void setNumOfCopy(int numOfCopy) {
        this.numOfCopy = numOfCopy;
    }

    public int getSalesManCustomers() {
        return salesManCustomers;
    }

    public void setSalesManCustomers(int salesManCustomers) {
        this.salesManCustomers = salesManCustomers;
    }

    public int getMinSalePric() {
        return minSalePric;
    }

    public void setMinSalePric(int minSalePric) {
        this.minSalePric = minSalePric;
    }

    public int getPrintMethod() {
        return printMethod;
    }

    public void setPrintMethod(int printMethod) {
        this.printMethod = printMethod;
    }

    public int getAllowOutOfRange() {
        return allowOutOfRange;
    }

    public void setAllowOutOfRange(int allowOutOfRange) {
        this.allowOutOfRange = allowOutOfRange;
    }

    public int getWorkOnline() {
        return workOnline;
    }

    public void setWorkOnline(int workOnline) {
        this.workOnline = workOnline;
    }
}
