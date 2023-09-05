import org.http4k.aws.AwsSdkClient
import org.http4k.client.JavaHttpClient
import org.http4k.connect.amazon.core.model.Region
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.debug
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class S3Test {
    @Test
    fun works() {
        val bucketName = BucketName.of("example-bucket")
        FakeS3().apply {
            s3Client().createBucket(bucketName, Region.EU_CENTRAL_1)
            debug().asServer(SunHttp(port = 26467)).start()
        }

        val client = ClientFilters.SetBaseUriFrom(Uri.of("http://${bucketName.value}.s3.eu-central-1.localhost:26467"))
            .then(JavaHttpClient())

        val s3Client: S3Client = S3Client.builder()
            .httpClient(AwsSdkClient(client))
            .region(software.amazon.awssdk.regions.Region.EU_CENTRAL_1)
            .build()

        val request = PutObjectRequest.builder()
            .bucket(bucketName.value)
            .key("this-is-a-key")
            .build()

        val body = RequestBody.fromString("test")

        s3Client.putObject(request, body)
    }
}