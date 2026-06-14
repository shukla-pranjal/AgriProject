import unittest
import urllib.request
import urllib.error
import json

class TestMicroservices(unittest.TestCase):

    def test_1_eureka_server(self):
        """Test Eureka Server is running and responding"""
        try:
            response = urllib.request.urlopen("http://localhost:8761/", timeout=5)
            self.assertEqual(response.status, 200)
            html = response.read().decode('utf-8')
            self.assertIn("Eureka", html)
        except Exception as e:
            self.fail(f"Eureka Server check failed: {e}")

    def test_2_gateway_health(self):
        """Test API Gateway health actuator"""
        try:
            response = urllib.request.urlopen("http://localhost:8080/actuator/health", timeout=5)
            self.assertEqual(response.status, 200)
            data = json.loads(response.read().decode('utf-8'))
            self.assertEqual(data.get("status"), "UP")
        except Exception as e:
            self.fail(f"API Gateway health check failed: {e}")

    def test_3_ml_direct_health(self):
        """Test Python ML Service directly on port 5000"""
        try:
            response = urllib.request.urlopen("http://localhost:5000/test", timeout=5)
            self.assertEqual(response.status, 200)
            data = json.loads(response.read().decode('utf-8'))
            self.assertEqual(data.get("status"), "running")
            self.assertEqual(data.get("service"), "ML Service")
        except Exception as e:
            self.fail(f"ML Service direct check failed: {e}")

    def test_4_ml_via_gateway(self):
        """Test ML Service through API Gateway routing"""
        try:
            response = urllib.request.urlopen("http://localhost:8080/ml-service/test", timeout=5)
            self.assertEqual(response.status, 200)
            data = json.loads(response.read().decode('utf-8'))
            self.assertEqual(data.get("status"), "running")
            self.assertEqual(data.get("service"), "ML Service")
        except Exception as e:
            self.fail(f"ML Service via Gateway check failed: {e}")

    def test_5_farmflow_direct_access_blocked(self):
        """Test that direct access to FarmFlow backend on port 8081 is blocked (403 Forbidden)"""
        try:
            urllib.request.urlopen("http://localhost:8081/test", timeout=5)
            self.fail("Direct access to FarmFlow backend was allowed when it should be blocked!")
        except urllib.error.HTTPError as e:
            self.assertEqual(e.code, 403)
            data = json.loads(e.read().decode('utf-8'))
            self.assertIn("Direct access not allowed", data.get("message", ""))
        except Exception as e:
            self.fail(f"Expected HTTP 403, got error: {e}")

    def test_6_farmflow_via_gateway_auth(self):
        """Test API Gateway routes public auth calls to FarmFlow backend successfully"""
        url = "http://localhost:8080/api/v1/auth/login"
        payload = json.dumps({
            "email": "nonexistent_user@example.com",
            "password": "password123"
        }).encode('utf-8')
        
        req = urllib.request.Request(
            url, 
            data=payload, 
            headers={"Content-Type": "application/json"},
            method="POST"
        )
        
        try:
            urllib.request.urlopen(req, timeout=5)
            self.fail("Expected HTTP 404 (User not found), but request succeeded")
        except urllib.error.HTTPError as e:
            self.assertEqual(e.code, 404)
            data = json.loads(e.read().decode('utf-8'))
            self.assertEqual(data.get("message"), "User not found")
        except Exception as e:
            self.fail(f"Expected HTTP 404, got error: {e}")

if __name__ == '__main__':
    unittest.main()
