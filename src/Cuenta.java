public class Cuenta {
    private final int idCuenta;
    private final String numeroCuenta;
    private final int saldo;

    public Cuenta(int idCuenta, String numeroCuenta, int saldo) {
        this.idCuenta = idCuenta;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public int getSaldo() {
        return saldo;
    }
}
