package euclid.integration;

import euclid.util.encryption.RC4;
import euclid.util.encryption.SecretKey;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ProtocolHarnessTest {

    private static Process serverProcess;
    private static final String WORKDIR = "/opt/git/Euclid/euclid-java";
    private static final String USER = "Alex";
    private static final String PASS = "123";

    @BeforeAll
    static void startServer() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "mvn", "-pl", "euclid", "exec:java",
                "-Dexec.mainClass=euclid.Euclid",
                "-Dexec.workingDirectory=.",
                "-q"
        );
        pb.directory(new java.io.File(WORKDIR));
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.to(new java.io.File("/tmp/euclid-server.log")));
        serverProcess = pb.start();
        Thread.sleep(20000);
    }

    @AfterAll
    static void stopServer() {
        if (serverProcess != null && serverProcess.isAlive()) {
            serverProcess.destroy();
        }
    }

    private GameClient connect(int port) throws Exception {
        return new GameClient("127.0.0.1", port);
    }

    @Test
    void testHelloOnConnect() throws Exception {
        GameClient client = connect(37120);
        String frame = client.readAllData(2000);
        assertNotNull(frame);
        assertTrue(frame.contains("HELLO"), "Should contain HELLO: " + frame);
        assertTrue(frame.startsWith("# "), "Frame should start with '# ': " + frame);
        client.close();
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("LOGIN nonexistentuser wrongpass");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive SYSTEMBROADCAST on login failure");
        assertTrue(response.contains("incorrect"), "Should contain 'incorrect': " + response);
        client.close();
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("GETCREDITS");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive WALLETBALANCE");
        assertTrue(response.contains("WALLETBALANCE"), "Should contain WALLETBALANCE: " + response);
        assertTrue(response.contains("999999"), "Should contain credits: " + response);
        client.close();
    }

    @Test
    void testVersionCheck() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("VERSIONCHECK RELEASE63-201302211227-193109692");
        String response = client.readAllData(3000);
        assertNotNull(response);
        assertTrue(response.contains("ENCRYPTION_ON"), "Should contain ENCRYPTION_ON: " + response);
        assertTrue(response.contains("SECRET_KEY"), "Should contain SECRET_KEY: " + response);
        client.close();
    }

    @Test
    void testApproveNameValid() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("APPROVENAME validname123");
        String response = client.readAllData(3000);
        assertNotNull(response);
        assertTrue(response.contains("NAME_APPROVED"), "Should contain NAME_APPROVED: " + response);
        client.close();
    }

    @Test
    void testApproveNameInvalid() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("APPROVENAME ab");
        String response = client.readAllData(3000);
        assertNotNull(response);
        assertTrue(response.contains("NAME_UNACCEPTABLE"), "Should contain NAME_UNACCEPTABLE: " + response);
        client.close();
    }

    @Test
    void testInfoRetrieve() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("INFORETRIEVE " + USER + " " + PASS);
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive USEROBJECT for valid credentials");
        assertTrue(response.contains("USEROBJECT"), "Should contain USEROBJECT: " + response);
        client.close();
    }

    @Test
    void testGetCredits() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("GETCREDITS");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive WALLETBALANCE");
        assertTrue(response.contains("WALLETBALANCE"), "Should contain WALLETBALANCE: " + response);
        client.close();
    }

    @Test
    void testGetStrip() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("GETSTRIP 0");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive STRIPINFO");
        assertTrue(response.contains("STRIPINFO"), "Should contain STRIPINFO: " + response);
        client.close();
    }

    @Test
    void testTryFlat() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("TRYFLAT 1");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive FLAT_LETIN");
        assertTrue(response.contains("FLAT_LETIN"), "Should contain FLAT_LETIN: " + response);
        client.close();
    }

    @Test
    void testSearchBusyFlats() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("SEARCHBUSYFLATS 0,50");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive BUSY_FLAT_RESULTS");
        assertTrue(response.contains("BUSY_FLAT_RESULTS"), "Should contain BUSY_FLAT_RESULTS: " + response);
        client.close();
    }

    @Test
    void testRegister() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        String testName = "testuser" + (System.currentTimeMillis() % 100000);
        String body = "REGISTER " + (char) 13
                + "name=" + testName + (char) 13
                + "password=testpass123" + (char) 13
                + "email=test@test.com" + (char) 13
                + "figure=0-0-0-0" + (char) 13
                + "directMail=0" + (char) 13
                + "birthday=1990-01-01" + (char) 13
                + "phonenumber=000" + (char) 13
                + "customData=" + (char) 13
                + "has_read_agreement=1" + (char) 13
                + "sex=Male" + (char) 13
                + "country=FI";
        client.send(body);
        Thread.sleep(2000);

        GameClient client2 = connect(37120);
        client2.readAllData(1000);
        client2.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);
        client2.send("INFORETRIEVE " + testName + " testpass123");
        String response = client2.readAllData(3000);
        assertNotNull(response, "Should be able to retrieve newly registered user");
        assertTrue(response.contains("USEROBJECT"), "Should contain USEROBJECT: " + response);
        client.close();
        client2.close();
    }

    @Test
    void testPublicServerLogin() throws Exception {
        GameClient client = connect(22009);
        String hello = client.readAllData(1000);
        assertNotNull(hello);
        assertTrue(hello.contains("HELLO"));

        client.send("LOGIN " + USER + " " + PASS);
        String response = client.readAllData(5000);
        assertNotNull(response, "Public server should send room data after LOGIN");
        assertTrue(response.contains("HEIGHTMAP"), "Should contain HEIGHTMAP: " + response);
        client.close();
    }

    @Test
    void testPrivateServerConnection() throws Exception {
        GameClient client = connect(25006);
        String hello = client.readAllData(1000);
        assertNotNull(hello);
        assertTrue(hello.contains("HELLO"));
        client.close();
    }

    @Test
    void testChatInRoom() throws Exception {
        GameClient client = connect(22009);
        client.readAllData(1000);

        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(3000);

        client.send("CHAT hello world");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive CHAT broadcast in room");
        assertTrue(response.contains("CHAT"), "Should contain CHAT: " + response);
        assertTrue(response.contains("hello world"), "Should contain message: " + response);
        client.close();
    }

    @Test
    void testGotoFlat() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("GOTOFLAT 1/1");
        String response = client.readAllData(5000);
        assertNotNull(response, "Should receive room entry data after GOTOFLAT");
        client.close();
    }

    @Test
    void testUnauthenticatedMessageDropped() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("GETCREDITS");
        String response = client.readAllData(3000);
        assertNull(response, "Unauthenticated GETCREDITS should be dropped (no response)");
        client.close();
    }

    @Test
    void testClientIp() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("CLIENTIP");
        String response = client.readAllData(2000);
        assertNull(response, "CLIENTIP on main server should not send a response");
        client.close();
    }

    @Test
    void testShout() throws Exception {
        GameClient client = connect(22009);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(3000);

        client.send("SHOUT hello everyone");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive SHOUT broadcast");
        assertTrue(response.contains("SHOUT"), "Should contain SHOUT: " + response);
        client.close();
    }

    @Test
    void testWhisper() throws Exception {
        GameClient client = connect(22009);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(3000);

        client.send("WHISPER " + USER + " hello whisper");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive WHISPER broadcast");
        assertTrue(response.contains("WHISPER"), "Should contain WHISPER: " + response);
        client.close();
    }

    @Test
    void testMoveInRoom() throws Exception {
        GameClient client = connect(22009);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(3000);

        client.send("Move 1 0 0 0 0 0");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive STATUS after Move");
        assertTrue(response.contains("STATUS"), "Should contain STATUS: " + response);
        client.close();
    }

    @Test
    void testGoAway() throws Exception {
        GameClient client = connect(22009);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(3000);

        client.send("GOAWAY");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive some response after GOAWAY");
        client.close();
    }

    @Test
    void testGetFlatInfo() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("GETFLATINFO 1/50");
        String response = client.readAllData(3000);
        assertNotNull(response, "Should receive FLATINFO for owned room");
        assertTrue(response.contains("FLATINFO"), "Should contain FLATINFO: " + response);
        client.close();
    }

    @Test
    void testFindUser() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);
        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1000);

        client.send("FINDUSER " + USER);
        String response = client.readAllData(2000);
        assertNull(response, "FINDUSER is a no-op in C# (no response)");
        client.close();
    }

    @Test
    void testEncryptionRoundTrip() throws Exception {
        GameClient client = connect(37120);
        client.readAllData(1000);

        client.send("VERSIONCHECK RELEASE63-201302211227-193109692");
        String response = client.readAllData(3000);
        assertNotNull(response);
        assertTrue(response.contains("ENCRYPTION_ON"), "Should contain ENCRYPTION_ON: " + response);
        assertTrue(response.contains("SECRET_KEY"), "Should contain SECRET_KEY: " + response);

        String publicKey = extractPublicKey(response);
        assertNotNull(publicKey, "Should extract public key from SECRET_KEY frame");

        RC4 rc4 = new RC4();
        rc4.setKey(SecretKey.secretDecode(publicKey));
        client.setEncryption(rc4);

        client.send("LOGIN " + USER + " " + PASS);
        Thread.sleep(1500);

        client.send("GETCREDITS");
        String creditsResponse = client.readAllData(3000);
        assertNotNull(creditsResponse, "Should receive WALLETBALANCE after encrypted LOGIN");
        assertTrue(creditsResponse.contains("WALLETBALANCE"), "Should contain WALLETBALANCE: " + creditsResponse);
        client.close();
    }

    private String extractPublicKey(String data) {
        int idx = data.indexOf("SECRET_KEY");
        if (idx < 0) return null;
        String after = data.substring(idx + "SECRET_KEY".length());
        int end = after.indexOf("##");
        if (end < 0) return null;
        String body = after.substring(0, end).trim();
        return body.isEmpty() ? null : body;
    }

    static class GameClient implements AutoCloseable {
        private final Socket socket;
        private final OutputStream out;
        private final InputStream in;
        private RC4 rc4;

        GameClient(String host, int port) throws Exception {
            socket = new Socket(host, port);
            socket.setSoTimeout(15000);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        }

        void setEncryption(RC4 rc4) {
            this.rc4 = rc4;
        }

        void send(String message) throws Exception {
            byte[] body = message.getBytes(StandardCharsets.ISO_8859_1);
            String lengthStr = String.format("%4d", body.length);
            String fullFrame = lengthStr + new String(body, StandardCharsets.ISO_8859_1);

            if (rc4 != null) {
                String encrypted = rc4.encipher(fullFrame);
                out.write(encrypted.getBytes(StandardCharsets.ISO_8859_1));
            } else {
                out.write(fullFrame.getBytes(StandardCharsets.ISO_8859_1));
            }
            out.flush();
        }

        String readAllData(long timeoutMs) throws Exception {
            long start = System.currentTimeMillis();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            long lastData = System.currentTimeMillis();

            while (System.currentTimeMillis() - start < timeoutMs) {
                if (in.available() > 0) {
                    int n = in.read(buf);
                    if (n == -1) break;
                    baos.write(buf, 0, n);
                    lastData = System.currentTimeMillis();
                } else {
                    if (baos.size() > 0 && System.currentTimeMillis() - lastData > 500) {
                        break;
                    }
                    Thread.sleep(25);
                }
            }
            return baos.size() > 0 ? baos.toString(StandardCharsets.ISO_8859_1) : null;
        }

        @Override
        public void close() throws Exception {
            socket.close();
        }
    }
}
