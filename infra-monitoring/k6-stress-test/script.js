import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 500,
    duration: '10m'
};

export default function () {
    let responses = http.batch([
        // ['GET', "https://www.ecsimsw.com/"],
        ['GET', "https://www.ecsimsw.com/api/view/total"]
    ])
    check(responses[0], {
        'check is ok': (r) => r.status === 200
    });
}
